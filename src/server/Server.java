package server;

public class Server
{
    public enum HttpServerStatus
    {
        Stopped,
        Active,
        NotCreated
    }

    public static WebServer webServer;
    private static WebServer httpServer;

    public static void CreateHTTPServer()
    {
        if (httpServer == null)
        {
            synchronized (webServer)
            {
                if (httpServer == null)
                {
                    httpServer = new WebServer(8080);
                    new Thread(httpServer).start();
                }
            }
        }
    }

    public static HttpServerStatus getHTTPServerStatus()
    {
        if (httpServer != null)
        {
            if (httpServer.isStopped)
                return HttpServerStatus.Stopped;
            else
                return HttpServerStatus.Active;
        }
        return HttpServerStatus.NotCreated;
    }

    public static void setHTTPServerStatus(HttpServerStatus s)
    {
        if (httpServer != null) {
            switch (s) {
                case Stopped:
                    httpServer.isStopped = true;
                    break;
                    case Active:
                        httpServer.isStopped = false;
                        break;
                        default:
                            break;
            }
        }
    }
}

