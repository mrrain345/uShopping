using Microsoft.AspNetCore.Mvc;
using uShopping.Models;
using uShopping.Data;
using System;
using System.Linq;
using System.Collections.Generic;

namespace uShopping.Controllers {

  [ApiController]
  [Route("[controller]")]
  public class ListsController : Controller {
    private DBContext db;
    public ListsController(DBContext context) {
      this.db = context;
    }

    [HttpGet]
    public ActionResult<IEnumerable <ProductList>> Get([FromHeader] Guid authorization) {

        var session = db.Sessions.SingleOrDefault(s => s.SessId == authorization);
        if(session == null) return NotFound();

        var listMembers = db.ListMembers.Where(lm => lm.UserId == session.UserId);

        return db.ProductLists.Join(listMembers, pl => pl.Id, lm => lm.ListId, (pl, lm) => pl).ToArray();
    }
  }
}