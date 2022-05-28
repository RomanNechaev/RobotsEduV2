package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static gui.WindowsCommon.*;

public class LogWindowSource
{
    private int mIQueueLength;

    private LinkedBlockingDeque<LogEntry> mMessages;
    private final ArrayList<LogChangeListener> mListeners;
    private volatile LogChangeListener[] mActiveListeners;

    public LogWindowSource(int iQueueLength)
    {
        mIQueueLength = iQueueLength;
        mMessages = new LinkedBlockingDeque<>(iQueueLength);
        mListeners = new ArrayList<LogChangeListener>();
    }

    public void registerListener(LogChangeListener listener)
    {
        synchronized(mListeners)
        {
            mListeners.add(listener);
            mActiveListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(mListeners)
        {
            mListeners.remove(listener);
            mActiveListeners = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage)
    {
        if(size() < mIQueueLength) {
            LogEntry entry = new LogEntry(logLevel, strMessage);
            mMessages.add(entry);
            LogChangeListener[] activeListeners = mActiveListeners;

            if (activeListeners == null) {
                synchronized (mListeners) {
                    if (mActiveListeners == null) {
                        activeListeners = mListeners.toArray(new LogChangeListener[0]);
                        mActiveListeners = activeListeners;
                    }
                }
            }
            if (activeListeners != null) {
                for (LogChangeListener listener : activeListeners) {
                        listener.onLogChanged();
                }
            }
        }
        else {
            if (confirmClearing()) {
                deleteOldEntry();
                Logger.debug("Очень новая строка");
            }
        }
    }

    public void deleteOldEntry()
    {
        mMessages.removeFirst();
        LogChangeListener[] activeListeners = mActiveListeners;

        for (LogChangeListener listener : activeListeners) {
            listener.onLogChanged();
        }
    }

    public void reset()
    {
        mMessages.clear();
        LogChangeListener[] activeListeners = mActiveListeners;

        for (LogChangeListener listener : activeListeners) {
            listener.onLogChanged();
        }
    }

    public int size()
    {
        return mMessages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        if (startFrom < 0 || startFrom >= mMessages.size())
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, mMessages.size());
        return mMessages.stream().skip(startFrom).limit(count).collect(Collectors.toList());
    }

    public Iterable<LogEntry> all()
    {
        return mMessages;
    }
}