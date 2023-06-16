import com.sshtools.client.SessionChannelNG;
import com.sshtools.client.SshClient;
import com.sshtools.client.shell.ExpectShell;
import com.sshtools.client.shell.ShellProcess;
import com.sshtools.client.shell.ShellTimeoutException;
import com.sshtools.client.tasks.ShellTask;
import com.sshtools.client.tasks.StatTask;
import com.sshtools.client.tasks.Task;
import com.sshtools.common.ssh.SshException;

import java.io.IOException;
import java.util.Scanner;

public class Deploy {
    public static void main(String[] args) throws IOException {
        String host = "iutnc-127-";

        //Scanner scanner = new Scanner(System.in);
        String password = "ODTPpeopOO53439:";//scanner.nextLine();

        int i = 7;
        String iFormat = Integer.toString(i);
        if (iFormat.length() == 1) iFormat = "0"+iFormat;
        /*for (int i = 224; i <= 246; i++) */{

            System.out.println(host+iFormat);

            try (SshClient connection = new SshClient(host+iFormat, 22, "demang198u", password.toCharArray())) {

                connection.runTask(new ShellTask(connection) {
                    @Override
                    protected void onOpenSession(SessionChannelNG sessionChannelNG) throws IOException, SshException, ShellTimeoutException {
                        ExpectShell shell = new ExpectShell(this);
                        ShellProcess process = shell.executeCommand("cd ~/nas/IdeaProjects/ProjetProgRep/out/production/ProjetProgRep/ && java Shard", true);
                    }
                });

            } catch (SshException ignored) {
            }
        }
    }
}
