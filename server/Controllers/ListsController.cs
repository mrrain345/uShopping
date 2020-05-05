using Microsoft.AspNetCore.Mvc;
using uShopping.Models;
using uShopping.Data;
using System;
using System.Linq;
using System.Collections.Generic;

namespace uShopping.Controllers {

  [ApiController]
  [Route("[controller]")]
  public partial class ListsController : Controller {
    private DBContext db;
    public ListsController(DBContext context) {
      this.db = context;
    }

    [HttpGet]
    public ActionResult<IEnumerable <ProductListData>> GetAll([FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return NotFound();

        var productLists = user.ListMembers.Select(lm => new ProductListData(lm.ProductList));
        return Ok(productLists);
    }
    [HttpGet("{id}")]
    public ActionResult<ProductListData> GetOne(Guid id, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return NotFound();

        var listMember = user.ListMembers.SingleOrDefault(lm => lm.ListId == id);
        if (listMember == null) return NotFound();

        return new ProductListData(listMember.ProductList);
    }

    [HttpPost]
    public ActionResult<ProductListData> PostList([FromBody] ProductListData body, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return NotFound();

        var productList = new ProductList {
            Id = Guid.NewGuid(),
            Title = body.Title
        };
        db.ProductLists.Add(productList);

        var listMember = new ListMember {
            Id = Guid.NewGuid(),
            UserId = user.Id,
            ListId = productList.Id
        };

        db.ListMembers.Add(listMember);
        db.SaveChanges();

        return new ProductListData(productList);
    }

    [HttpPatch("{id}")]
    public ActionResult<ProductListData> Patch(Guid id, [FromBody] ProductListData body, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return NotFound();

        var listMember = user.ListMembers.SingleOrDefault(lm => lm.ListId == id);
        if (listMember == null) return NotFound();
        var productList = listMember.ProductList;
        
        if (body.Title != null) productList.Title = body.Title;
        db.SaveChanges();

        return new ProductListData(productList);
    }

    [HttpDelete("{id}")]
    public ActionResult<ProductListIdData> Delete(Guid id, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return NotFound();

        var listMember = user.ListMembers.SingleOrDefault(lm => lm.ListId == id);
        if (listMember == null) return NotFound();
        var productList = listMember.ProductList;

        foreach(var lm in productList.ListMembers) {
            db.ListMembers.Remove(lm);
        }
        db.ProductLists.Remove(productList);

        db.SaveChanges();

        return new ProductListIdData(productList);
    }
  }
}
