using Microsoft.AspNetCore.Mvc;
using uShopping.Models;
using uShopping.Data;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using System;
using System.Security.Cryptography;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;

namespace uShopping.Controllers {

  [ApiController]
  [Route("[controller]")]
  public class UsersController : Controller {

    private DBContext db;
    public UsersController(DBContext context) {
      this.db = context;
    }

    [HttpGet("{id}")]
    public ActionResult<UserData> Get(Guid id) {
      var user = db.Users.SingleOrDefault(u => u.Id == id);
      if (user == null) return NotFound();
      return new UserData(user);
    }

    [HttpPost]
    public ActionResult<UserData> Post([FromBody] SignUpData data) {
      var email = db.Users.SingleOrDefault(u => u.Email == data.Email);
      if (data.Username.Length < 3) return BadRequest(new ErrorData(1, "Username is to short (min 3 characters)"));
      if (data.Username.Length > 80) return BadRequest(new ErrorData(2, "Username is to long (max 80 characters)"));
      if (email != null) return BadRequest(new ErrorData(3, "Email address has been used"));
      if (data.Password.Length < 8) return BadRequest(new ErrorData(4, "Password is to short (min 8 characters)"));
      if (data.Password != data.ConfirmPassword) return BadRequest(new ErrorData(5, "Passwords don't match"));

      var pass = PasswordHash(data.Password);

      User user = new User {
        Id = Guid.NewGuid(),
        Username = data.Username,
        Password = pass,
        Email = data.Email
      };

      Console.WriteLine(pass);
      
      db.Users.Add(user);
      db.SaveChanges();
      return new UserData(user);
    }



    string GenerateSalt() {
      byte[] salt = new byte[128 / 8];
      using (var random = RandomNumberGenerator.Create()) {
        random.GetBytes(salt);
      }
      return Convert.ToBase64String(salt);
    }

    string PasswordHash(string password, string salt = null) {
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

    bool PasswordVerify(string password, string hash) {
      string[] split = hash.Split(":");
      return PasswordHash(password, split[0]) == hash;
    }
  }
}