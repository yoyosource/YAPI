package yapi.ui.console;

import java.util.LinkedList;
import java.util.List;

class ConsoleMessagePreRender extends ConsoleMessage {

    /*public static ConsoleMessagePreRender getInstance(Console console) {
        ConsoleMessagePreRender messagePreRender = new ConsoleMessagePreRender(List.of());
        messagePreRender.width = console.getWidth();
        return messagePreRender;
    }*/

    private int width = 0;
    private ConsoleAlignment alignment = ConsoleAlignment.LEFT;
    private ConsoleClipping clipping = ConsoleClipping.WRAP_OFF;

    List<ConsoleMessageTask> preRendered = new LinkedList<>();
    private List<ConsoleMessageTask> toRender = new LinkedList<>();

    private ConsoleMessagePreRender(List<ConsoleMessageTask> tasks) {
        super(tasks);
    }

    public ConsoleMessagePreRender appendMessage(ConsoleMessage message) {
        getTasks().addAll(message.getTasks());
        toRender.addAll(message.getTasks());
        return this;
    }

}
