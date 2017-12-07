package software.sham.sftp;

import org.apache.commons.io.FileUtils;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.sham.ssh.MockSshServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class MockSftpServer extends MockSshServer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Path baseDirectory;

    public MockSftpServer(int port) throws IOException {
        this(port, false);
    }

    private MockSftpServer(int port, boolean enableShell) throws IOException {
        super(port, false);
        initSftp();
        if (enableShell) {
            enableShell();
        }
        start();
    }

    private void initSftp() {
        sshServer.setCommandFactory(new ScpCommandFactory());
        sshServer.setSubsystemFactories(Arrays.<NamedFactory<Command>>asList(new SftpSubsystemFactory()));
    }

    public Path getBaseDirectory() {
        return baseDirectory;
    }

    @Override
    public void start() throws IOException {
        baseDirectory = Files.createTempDirectory("sftproot");
        sshServer.setFileSystemFactory(new VirtualFileSystemFactory(baseDirectory.toAbsolutePath().toString()));
        super.start();
    }

    @Override
    public void stop() throws IOException {
        super.stop();
        FileUtils.deleteQuietly(baseDirectory.toFile());
    }

    public static MockSftpServer createWithShell(int port) throws IOException {
        return new MockSftpServer(port, true);
    }
}
