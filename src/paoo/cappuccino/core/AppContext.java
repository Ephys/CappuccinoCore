package paoo.cappuccino.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import paoo.cappuccino.util.exception.FatalException;

/**
 * Application environment-specific configuration.
 *
 * @author Guylian Cox
 */
public class AppContext {

  private final List<CrashListener> crashListeners = new ArrayList<>();
  private final Logger appLogger;
  private final Profile profileType;
  private final String profile;
  private final String appName;
  private final String version;

  /**
   * Configures the environment using an hardcoded profile.
   *
   * @param appName The name of the application.
   * @param version The version of the application.
   * @param profile The name of the profile to load for this instance.
   */
  public AppContext(String appName, String version, String profile) {
    addCrashListener(new CrashWriter(this));

    this.profile = profile == null ? "prod" : profile;
    this.profileType = fetchProfile();

    this.appName = appName;
    this.version = version;

    appLogger = initLogger();
    initGlobalCatcher();
  }

  /**
   * Configures the environment. The profile to load is set from the jvm arguments.
   *
   * @param appName The name of the application.
   * @param version The version of the application.
   */
  public AppContext(String appName, String version) {
    this(appName, version, System.getProperty("profile"));
  }

  /**
   * Parses the profile name and extrapolates the profile type from it.
   */
  private Profile fetchProfile() {
    if (profile.startsWith("prod")) {
      return Profile.PROD;
    } else if (profile.startsWith("dev")) {
      return Profile.DEV;
    } else {
      return Profile.TEST;
    }
  }

  /**
   * Creates the application global catcher.
   */
  private void initGlobalCatcher() {
    Thread.setDefaultUncaughtExceptionHandler((thread, exception) -> {
      appLogger.log(Level.SEVERE, "Fatal exception", exception);

      for (CrashListener listener : crashListeners) {
        listener.onCrash(exception);
      }

      System.exit(1);
    });
  }

  /**
   * Setups the application main logger.
   */
  private Logger initLogger() {
    Logger appLogger = Logger.getLogger(appName);

    try {
      Formatter formatter = new Formatter() {
        @Override
        public String format(LogRecord record) {
          StringBuilder str = new StringBuilder();

          str.append('[')
              .append(LocalDateTime.now()
                          .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")))
              .append("][").append(record.getLoggerName()).append("][")
              .append(record.getLevel().getLocalizedName()).append("] ")
              .append(record.getMessage()).append("\r\n");

          Throwable throwable = record.getThrown();
          if (throwable != null) {
            StringWriter stringWriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringWriter));
            str.append(stringWriter).append("\r\n");
          }

          return str.toString();
        }
      };

      Handler logFile = new FileHandler("app.log");
      logFile.setFormatter(formatter);
      logFile.setLevel(Level.ALL);

      Handler logConsole = new ConsoleHandler() {
        public void publish(LogRecord record) {
          try {
            String message = getFormatter().format(record);
            if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
              System.err.print(message);
            } else {
              System.out.print(message);
            }
          } catch (Exception e) {
            throw new FatalException("Logger error", e);
          }
        }
      };
      logConsole.setFormatter(formatter);

      appLogger.addHandler(logConsole);
      appLogger.addHandler(logFile);

      appLogger.setParent(Logger.getGlobal());
      appLogger.setUseParentHandlers(false);
    } catch (IOException e) {
      throw new FatalException("Could not setup the application logger", e);
    }

    return appLogger;
  }

  /**
   * Creates a logger for a part of the application.
   *
   * @param appLayer The part of the application requesting a logger (IHM, DAL, DALSQL, UCC, ...).
   * @return a logger.
   */
  public Logger getLogger(String appLayer) {
    Logger layerLogger = Logger.getLogger(appName + "-" + appLayer);
    layerLogger.setParent(appLogger);

    return layerLogger;
  }

  /**
   * Returns the application parent/global logger.
   */
  public Logger getAppLogger() {
    return appLogger;
  }

  /**
   * <p>
   * Gets the application profile, defaults to PROFILES.PROD if not set. This variable allows the
   * application to know whether the system is in production, in development or in tests.
   * </p>
   *
   * <p>
   * It can be changed using the "-Dprofile" flag when running the application: "prod" for a
   * production environment, "test" for a testing environment and "dev" for a development
   * environment.
   * </p>
   *
   * @return the current profile type.
   */
  public Profile getProfileType() {
    return profileType;
  }

  /**
   * Gets the application profile name.
   */
  public String getProfile() {
    return profile;
  }

  /**
   * Gets the application version.
   */
  public String getVersion() {
    return version;
  }

  /**
   * Gets the application name.
   */
  public String getAppName() {
    return appName;
  }

  /**
   * Adds a listener that will be called just before the application crashes.
   *
   * @param listener the listener to call.
   * @return true: the listener has been added.
   */
  public boolean addCrashListener(CrashListener listener) {
    return this.crashListeners.add(listener);
  }

  /**
   * Removes a crash listener.
   *
   * @param listener the listener to remove.
   * @return true: the listener has been removed.
   */
  public boolean removeCrashListener(CrashListener listener) {
    return this.crashListeners.remove(listener);
  }

  public enum Profile {
    PROD, TEST, DEV
  }
}
