using Microsoft.AspNetCore.Mvc;
using uShopping.Models;
using uShopping.Data;
using Microsoft.EntityFrameworkCore;
using System.Linq;
using System;

namespace uShopping.Controllers {

  [ApiController]
  [Route("[controller]")]
  public class SessionController : Controller {

    private DBContext db;
    public SessionController(DBContext context) {
        this.db = context;
    }

    [HttpGet]
    public ActionResult<SessionGetData> Get([FromHeader] Guid authorization) {
        Session session = Session.GetSession(db, authorization);
        if (session == null) return new SessionGetData(null);
        session.UpdatedAt = DateTime.Now;
        db.Sessions.Update(session);
        db.SaveChanges();

        return new SessionGetData(session.User);
    }

    [HttpPost]
    public ActionResult<SessionPostData> Post([FromBody] LoginData data) {
        var user = db.Users.SingleOrDefault((u => u.Email == data.Email));
        if (user == null) return NotFound();
        if (!Session.PasswordVerify(data.Password, user.Password)) return NotFound();
        
        Session session = new Session {
            SessId = Guid.NewGuid(),
            UserId = user.Id
        };

        db.Sessions.Add(session);
        db.SaveChanges();
        return new SessionPostData(session.SessId, user);
    }

    [HttpDelete]
    public ActionResult Delete([FromHeader] Guid authorization) {
        var session = Session.GetSession(db, authorization);
        if (session == null) return NotFound();

        db.Sessions.Remove(session);
        db.SaveChanges();
        return Ok();
    }
  }
}