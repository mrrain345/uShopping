using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;

namespace uShopping.Models
{
    public partial class Session
    {
        public Guid SessId { get; set; }
        public Guid UserId { get; set; }
        public DateTime CreatedAt { get; set; }
        public DateTime UpdatedAt { get; set; }

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

        static string GenerateSalt() {
            byte[] salt = new byte[128 / 8];
            using (var random = RandomNumberGenerator.Create()) {
                random.GetBytes(salt);
            }
            return Convert.ToBase64String(salt);
        }

        public static string PasswordHash(string password, string salt = null) {
            if (salt == null) salt = GenerateSalt();

            string hashed = Convert.ToBase64String(KeyDerivation.Pbkdf2(
                password: password,
                salt: Convert.FromBase64String(salt),
                prf: KeyDerivationPrf.HMACSHA1,
                iterationCount: 10000,
                numBytesRequested: 256 / 8
            ));

            return salt + ":" + hashed;
        }

        public static bool PasswordVerify(string password, string hash) {
            string[] split = hash.Split(":");
            return PasswordHash(password, split[0]) == hash;
        }
    }
}
