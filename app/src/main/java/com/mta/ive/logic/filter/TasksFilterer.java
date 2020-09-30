package com.mta.ive.logic.filter;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mta.ive.logic.device.DeviceManager;
import com.mta.ive.logic.task.Task;
import com.mta.ive.logic.users.UserSettings;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TasksFilterer {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Task> getOnlyRelevantTasks(List<Task> tasksToFilter, UserSettings userSettings){
        List<Task> afterFilteringTasks;
        List<Task> relevantByDate = removeOutDatedTasks(tasksToFilter);

        if (userSettings.getPriorityType() == UserSettings.PRIORITY_TYPE.EFFICIENCY){
            afterFilteringTasks = getFilteredTasksByEfficiency(relevantByDate, userSettings);
        }
        else {
            afterFilteringTasks = getFilteredTasksByUrgency(relevantByDate, userSettings);
        }

        return afterFilteringTasks.size() > 4? afterFilteringTasks.subList(0, 4): afterFilteringTasks;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private static List<Task> removeOutDatedTasks(List<Task> tasksToFilter) {
        LocalDate today = DeviceManager.getInstance().getDate();
        List<Task> stillRelevantTasks = new ArrayList<>();

        tasksToFilter.forEach(task -> {
            boolean taskHasEndDate = task.getDeadLineDate() != null && !task.getDeadLineDate().equals("");

            if (taskHasEndDate){
                LocalDate taskLastDate;
                try {
                    taskLastDate = LocalDate.from(LocalDate.parse(task.getDeadLineDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
                catch (Exception e){
                    taskLastDate = LocalDate.from(LocalDate.parse(task.getDeadLineDate(), DateTimeFormatter.ofPattern("dd/M/yyyy")));
                }

                if (!taskLastDate.isBefore(today)){
                    stillRelevantTasks.add(task);
                }
            }
            else{
                stillRelevantTasks.add(task);
            }
        });

        return stillRelevantTasks;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private static List<Task> getFilteredTasksByUrgency(List<Task> tasksToFilter, UserSettings userSettings) {
        tasksToFilter.sort((a, b) -> a.getPriority() - b.getPriority());

        List<Task> taskCanBeDoneToday = getTasksByTimeLeft(tasksToFilter, userSettings);

        return taskCanBeDoneToday;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static List<Task> getFilteredTasksByEfficiency(List<Task> tasksToFilter, UserSettings userSettings) {
        tasksToFilter.sort((a, b) -> a.getDuration() - b.getDuration());

        List<Task> taskCanBeDoneToday = getTasksByTimeLeft(tasksToFilter, userSettings);

        return taskCanBeDoneToday;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static List<Task> getTasksByTimeLeft(List<Task> tasksToFilter, UserSettings userSettings){
        List<Task> tasksCanBeDoneByEOD = new ArrayList<>();
        int userDayEndHour = userSettings.getDayEndHour();
        int userDayEndMinute = userSettings.getDayEndMinutes();
        LocalTime endTime = LocalTime.MIN.plusHours(userDayEndHour).plusMinutes(userDayEndMinute);
        LocalTime currentTime = DeviceManager.getInstance().getTime();

        Duration duration = Duration.between(currentTime, endTime);
        int minutesLeft = (int) duration.toMinutes();
        AtomicInteger totalTasksDuration = new AtomicInteger();

        tasksToFilter.forEach(task -> {
            if (task.getDuration() <= minutesLeft && task.getDuration() <= minutesLeft - totalTasksDuration.get()){
                tasksCanBeDoneByEOD.add(task);
                totalTasksDuration.addAndGet(task.getDuration());
            }
        });
        return tasksCanBeDoneByEOD;
    }


}
