using Microsoft.AspNetCore.Mvc;
using uShopping.Models;
using uShopping.Data;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using System;

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
      if (user == null) return ErrorData.SessionError();
      return new UserData(user);
    }

    [HttpPost]
    public ActionResult<UserData> Post([FromBody] SignUpData data) {
      if (data.Username.Length < 3) return BadRequest(new ErrorData("Full name is too short"));
      if (!IsValidEmail(data.Email)) return BadRequest(new ErrorData("Bad email address"));
      if (data.Username.Length > 80) return BadRequest(new ErrorData("Full name is too long (max 80 characters)"));
      var email = db.Users.SingleOrDefault(u => u.Email == data.Email);
      if (email != null) return BadRequest(new ErrorData("Email address has been used"));
      if (data.Password.Length < 6) return BadRequest(new ErrorData("Password is too short (min 6 characters)"));
      if (data.Password != data.ConfirmPassword) return BadRequest(new ErrorData("Passwords don't match"));

      var pass = Session.PasswordHash(data.Password);

      User user = new User {
        Id = Guid.NewGuid(),
        Username = data.Username,
        Password = pass,
        Email = data.Email
      };
      
      db.Users.Add(user);
      db.SaveChanges();
      return new UserData(user);
    }

    bool IsValidEmail(string email) {
      try {
        var addr = new System.Net.Mail.MailAddress(email);
        return addr.Address == email;
      }
      catch {
        return false;
      }
    }
  }
}