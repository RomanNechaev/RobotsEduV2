package gui.frames;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener
{
    private LogWindowSource mLogSource;
    private TextArea mLogContent;

    public LogWindow(LogWindowSource logSource) 
    {
        super("Протокол работы", true, true, true, true);
        mLogSource = logSource;
        mLogSource.registerListener(this);
        mLogContent = new TextArea("");
        mLogContent.setSize(200, 500);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mLogContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        setName("LogWindow");
        pack();
        updateLogContent();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : mLogSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        mLogContent.setText(content.toString());
        mLogContent.invalidate();
    }
    
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
