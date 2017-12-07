package software.sham.ssh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.sham.ssh.actions.Action;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class SshResponder {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final List<Action> actions = new LinkedList<>();

    public void respond(MockSshShell shell) {
        for (Action action : actions) {
            try {
                action.respond(shell);
            } catch (IOException e) {
                logger.warn("Mock SSH error during response {}: {}", action.toString(), e.getMessage());
            }
        }
    }

    public void add(Action action) {
        actions.add(action);
    }

    public static SshResponder NULL = new SshResponder();
}
