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
      var email = db.Users.SingleOrDefault(u => u.Email == data.Email);
      if (data.Username.Length < 3) return BadRequest(new ErrorData(1, "Username is to short (min 3 characters)"));
      if (data.Username.Length > 80) return BadRequest(new ErrorData(2, "Username is to long (max 80 characters)"));
      if (email != null) return BadRequest(new ErrorData(3, "Email address has been used"));
      if (data.Password.Length < 6) return BadRequest(new ErrorData(4, "Password is to short (min 6 characters)"));
      if (data.Password != data.ConfirmPassword) return BadRequest(new ErrorData(5, "Passwords don't match"));

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
  }
}