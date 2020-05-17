using System;
using System.Threading;
using System.Threading.Tasks;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using uShopping.Models;
using System.Linq;

namespace uShopping {
internal class DBCleanService : IHostedService, IDisposable {
  
      private Timer timer;
      private IServiceScopeFactory scopeFactory;



      public DBCleanService(IServiceScopeFactory scopeFactory) {
          this.scopeFactory = scopeFactory;
      }

      public Task StartAsync(CancellationToken cancellationToken) {
          DateTime date = DateTime.Today + TimeSpan.FromHours(27);
          TimeSpan time = date - DateTime.Now;

          // Run every day on 3am
          timer = new Timer(ClearSessions, null, time, TimeSpan.FromDays(1));

          return Task.CompletedTask;
      }

      private void ClearSessions(object state) {
          using (var scope = scopeFactory.CreateScope()) {
            var db = scope.ServiceProvider.GetRequiredService<DBContext>();
            DateTime date = DateTime.Now - TimeSpan.FromDays(30);

            Console.WriteLine(db.Sessions.Count());
            var sessions = db.Sessions.Where(s => s.UpdatedAt < date);
            db.Sessions.RemoveRange(sessions);
            db.SaveChanges();
          }
      }

      public Task StopAsync(CancellationToken cancellationToken) {
          timer?.Change(Timeout.Infinite, 0);
          return Task.CompletedTask;
      }

      public void Dispose() {
          timer?.Dispose();
      }
  }
}