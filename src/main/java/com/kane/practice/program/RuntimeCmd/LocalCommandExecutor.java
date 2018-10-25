package com.kane.practice.program.RuntimeCmd;

public interface LocalCommandExecutor {

        ExecuteResult executeCommand(String command, long timeout);

}
