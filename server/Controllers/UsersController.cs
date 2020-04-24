using Microsoft.AspNetCore.Mvc;
using uShopping.Models;
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

    [HttpGet]
    public DbSet<User> GetAll() {
      return db.Users;
    }

    [HttpGet("{id}")]
    public ActionResult<User> Get(Guid id) {
      var usr = from user in db.Users where user.Id == id select user;

      if (usr.Count() != 1) return NotFound();
      return usr.FirstOrDefault();
    }

    [HttpPost]
    public string Post([FromBody] User user) {
      return user.Username;
    }
  }
}