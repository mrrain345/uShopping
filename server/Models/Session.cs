using System;
using System.Collections.Generic;
using System.Linq;

namespace uShopping.Models
{
    public partial class Session
    {
        public Guid SessId { get; set; }
        public Guid UserId { get; set; }
        public DateTime CreatedAt { get; set; }

        public virtual User User { get; set; }

        public static Session GetSession(DBContext db, Guid authorization) {
          return db.Sessions.SingleOrDefault(s => s.SessId == authorization);
        }

        public static User GetUser(DBContext db, Guid authorization) {
          Session session = Session.GetSession(db, authorization);
          if (session == null) {
            Console.WriteLine($"Session '{authorization}' not found");
            return null;
          }

          User user = session.User;
          if (user == null) Console.WriteLine($"User '{authorization}' not found");
          
          return user;
        }
    }
}
