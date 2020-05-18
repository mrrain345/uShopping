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
        if (user == null) return ErrorData.SessionError();

        var productLists = user.GetProductLists().Select(pl => new ProductListData(pl));
        return Ok(productLists);
    }
    [HttpGet("{id}")]
    public ActionResult<ProductListData> GetOne(Guid id, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return ErrorData.SessionError();

        var productList = user.GetProductList(id);
        if (productList == null) return NotFound();

        return new ProductListData(productList);
    }

    [HttpPost]
    public ActionResult<ProductListData> PostList([FromBody] ProductListData body, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return ErrorData.SessionError();

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
        if (user == null) return ErrorData.SessionError();

        var productList = user.GetProductList(id);
        if (productList == null) NotFound();
        
        if (body.Title != null) productList.Title = body.Title;

        db.SaveChanges();
        return new ProductListData(productList);
    }

    [HttpDelete("{id}")]
    public ActionResult<IdData> Delete(Guid id, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return ErrorData.SessionError();

        var productList = user.GetProductList(id);
        if (productList == null) NotFound();

        foreach(var lm in productList.ListMembers) db.ListMembers.Remove(lm);
        db.ProductLists.Remove(productList);

        db.SaveChanges();
        return new IdData(productList.Id);
    }
  }
}
