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
    public ActionResult<IEnumerable <ProductListData>> Get([FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return NotFound();

        var productLists = user.ListMembers.Select(lm => new ProductListData(lm.ProductList));
        return Ok(productLists);
    }

    [HttpPost]
    public ActionResult<ProductListData> Post([FromBody] TitleData data, [FromHeader] Guid authorization) {
        var session = db.Sessions.SingleOrDefault(s => s.SessId == authorization);
        if(session == null) return NotFound();

        var productList = new ProductList {
            Id = Guid.NewGuid(),
            Title = data.Title
        };
        db.ProductLists.Add(productList);

        var listMember = new ListMember {
            Id = Guid.NewGuid(),
            UserId = session.UserId,
            ListId = productList.Id
        };

        db.ListMembers.Add(listMember);
        db.SaveChanges();

        return new ProductListData(productList);
    }
  }
}