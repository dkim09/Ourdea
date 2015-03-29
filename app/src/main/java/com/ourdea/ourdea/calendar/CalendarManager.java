package com.ourdea.ourdea.calendar;

import android.content.Context;
import android.provider.CalendarContract;

import com.ourdea.ourdea.calendar.providerwrapper.Event;
import com.ourdea.ourdea.calendar.providerwrapper.Reminder;
import com.ourdea.ourdea.dto.TaskDto;
import com.ourdea.ourdea.resources.ApiUtilities;

import java.util.List;

public class CalendarManager {

    private Context context;

    private static CalendarManager calendarManager;

    private CalendarManager(Context context) {
        this.context = context;
    }

    public static CalendarManager getInstance(Context context) {
        if (calendarManager == null) {
            calendarManager = new CalendarManager(context);
        }

        return calendarManager;
    }

    public void createEvent(TaskDto task) {
        Event newEvent = new Event();

        newEvent.title = buildEventTitleFromTaskAndProject(task);
        newEvent.description = task.getDescription();
        newEvent.organizer = task.getId();
        newEvent.startDate = String.valueOf(task.getDueDate());
        newEvent.endDate = String.valueOf(task.getDueDate());
        newEvent.create(context.getContentResolver());

        Reminder reminder = new Reminder();
        reminder.method = CalendarContract.Reminders.METHOD_EMAIL;
        reminder.minutesBefore = 30;
        reminder.addToEvent(context.getContentResolver(), newEvent);
    }

    private String buildEventTitleFromTaskAndProject(TaskDto task) {
        return ApiUtilities.Session.getProjectName(context) + ": " + task.getName();
    }

    public void updateTask(TaskDto task) {
        Event eventToUpdate = findEventByTaskId(task.getId());

        if (eventToUpdate != null) {
            boolean dueDateChanged = !eventToUpdate.startDate.equals(String.valueOf(task.getDueDate()));

            eventToUpdate.title = buildEventTitleFromTaskAndProject(task);
            eventToUpdate.description = task.getDescription();
            eventToUpdate.startDate = String.valueOf(task.getDueDate());
            eventToUpdate.endDate = String.valueOf(task.getDueDate());
            eventToUpdate.organizer = task.getId();
            eventToUpdate.update(context.getContentResolver());

            if (dueDateChanged) {
                Reminder reminder = new Reminder();
                reminder.method = CalendarContract.Reminders.METHOD_EMAIL;
                reminder.minutesBefore = 30;
                reminder.addToEvent(context.getContentResolver(), eventToUpdate);
            }
        }
    }

    public void deleteTask(TaskDto task) {
        Event eventToDelete = findEventByTaskId(task.getId());
        if (eventToDelete != null) {
            eventToDelete.delete(context.getContentResolver());
        }
    }

    private Event findEventByTaskId(String taskId) {
        final String query = "(" + CalendarContract.Events.ORGANIZER + " = ?)";
        final String[] args = new String[]{ taskId };

        List<Event> eventList = Event.getEventsForQuery(query, args, null, context.getContentResolver());
        if (eventList.size() != 0) {
            return eventList.get(0);
        }

        return null;
    }
}
