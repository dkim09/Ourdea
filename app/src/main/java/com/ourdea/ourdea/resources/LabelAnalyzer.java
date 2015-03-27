package com.ourdea.ourdea.resources;

import com.ourdea.ourdea.dto.TaskDto;
import com.ourdea.ourdea.fragments.TaskListContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LabelAnalyzer {
    List<String> labelList;
    HashMap<String, Integer> toDoMap;
    HashMap<String, Integer> inProgressMap;
    HashMap<String, Integer> doneMap;

    public LabelAnalyzer() {
        toDoMap = new HashMap<>();
        inProgressMap = new HashMap<>();
        doneMap = new HashMap<>();
        labelList = new ArrayList<>();
    }

    public void addToDoTaskList(TaskListContent taskList) {
        for (TaskDto t : taskList.getTaskItems()) {
            String label = t.getLabel();
            if (!labelList.contains(label))
                labelList.add(label);

            if (!toDoMap.containsKey(label))
                toDoMap.put(label, new Integer(1));
            else
                toDoMap.put(label, new Integer(toDoMap.get(label).intValue() + 1));
        }
    }
    public void addInProgressTaskList(TaskListContent taskList) {
        for (TaskDto t : taskList.getTaskItems()) {
            String label = t.getLabel();
            if (!labelList.contains(label))
                labelList.add(label);

            if (!inProgressMap.containsKey(label))
                inProgressMap.put(label, new Integer(1));
            else
                inProgressMap.put(label, new Integer(inProgressMap.get(label).intValue() + 1));
        }
    }

    public void addDoneTaskList(TaskListContent taskList) {
        for (TaskDto t : taskList.getTaskItems()) {
            String label = t.getLabel();
            if (!labelList.contains(label))
                labelList.add(label);

            if (!doneMap.containsKey(label))
                doneMap.put(label, new Integer(1));
            else
                doneMap.put(label, new Integer(doneMap.get(label).intValue() + 1));
        }
    }

    public String getLabels() {
        String labels = "";
        for (String s: labelList) {

            labels += s + ",";
        }
        //Remove last comma
        if (labels.length() > 0)
            labels = labels.substring(0,labels.length() - 1);
        return labels;
    }

    public String getToDo() {
        String toDo = "";
        for (String s: labelList) {
            if (!toDoMap.containsKey(s))
                toDo += "0,";
            else
                toDo += toDoMap.get(s).toString() + ",";
        }
        if (toDo.length() > 0)
            toDo = toDo.substring(0,toDo.length()-1);
        return toDo;
    }

    public String getInProgress() {
        String inProgress = "";
        for (String s: labelList) {
            if (!inProgressMap.containsKey(s))
                inProgress += "0,";
            else
                inProgress += inProgressMap.get(s).toString() + ",";
        }
        if (inProgress.length() > 0)
            inProgress = inProgress.substring(0,inProgress.length()-1);
        return inProgress;
    }

    public String getDone() {
        String done = "";
        for (String s: labelList) {
            if (!doneMap.containsKey(s))
                done += "0,";
            else
                done += doneMap.get(s).toString() + ",";
        }
        if (done.length() > 0)
            done = done.substring(0,done.length()-1);
        return done;
    }
}
