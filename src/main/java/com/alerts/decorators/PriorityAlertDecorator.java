package com.alerts.decorators;
import com.alerts.alert_types.Alert;
public class PriorityAlertDecorator extends AlertDecorator{
    private int priority;

    public PriorityAlertDecorator(Alert decoratedAlert, int priority){
        super(decoratedAlert);
        this.priority = priority;
    }

    public int getPriority(){
        return this.priority;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    @Override
    public String getCondition(){
        return "[Priority: " + priority + "]" + decoratedAlert.getCondition();
    }
}
