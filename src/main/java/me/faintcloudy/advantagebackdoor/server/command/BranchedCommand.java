package me.faintcloudy.advantagebackdoor.server.command;

import me.faintcloudy.advantagebackdoor.server.Log4jServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class BranchedCommand extends ServerCommand {
    private final List<Method> subCommandMethods = new ArrayList<>();
    public BranchedCommand(String name, String introduce) {
        super(name, introduce);
        Class<? extends BranchedCommand> theClass = this.getClass();
        for (Method method : theClass.getMethods()) {
            if (method.isAnnotationPresent(SubCommand.class))
                subCommandMethods.add(method);
        }
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2)
        {
            for (String s : getHelpList()) {
                Log4jServer.getInstance().getLogger().info(s);
            }
            return;
        }
        for (Method method : subCommandMethods) {
            SubCommand annotation = method.getAnnotation(SubCommand.class);
            if (annotation.value().equalsIgnoreCase(args[1])) {
                try {
                    if (Arrays.equals(method.getParameterTypes(), new Class[]{String[].class}))
                        method.invoke(this, (Object) args);
                    else if (method.getParameterTypes().length == 0)
                        method.invoke(this);
                    return;
                } catch (InvocationTargetException | IllegalAccessException e) {
                    Log4jServer.getInstance().getLogger().error("在执行指令期间发生错误: ");
                    e.printStackTrace();
                }
            }

        }
        for (String s : this.getHelpList()) {
            Log4jServer.getInstance().getLogger().info(s);
        }


    }

    public List<String> getHelpList()
    {
        List<String> helpList = new ArrayList<>();
        for (Method method : subCommandMethods) {
            SubCommand command = method.getAnnotation(SubCommand.class);
            helpList.add(command.usage() + " - " + command.introduce());
        }

        return helpList;
    }
}
