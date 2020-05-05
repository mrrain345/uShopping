using Microsoft.AspNetCore.Mvc;
using uShopping.Models;
using uShopping.Data;
using System;
using System.Linq;
using System.Collections.Generic;

namespace uShopping.Controllers {

  public partial class ListsController : Controller {
    

    [HttpGet("{listId}/products")]
    public ActionResult<IEnumerable <ProductData>> GetProducts(Guid listId, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return NotFound();

        var listMember = user.ListMembers.SingleOrDefault(lm => lm.ListId == listId);
        if (listMember == null) return NotFound();

        var products = listMember.ProductList.Products.Select(p => new ProductData(p));
        return Ok(products);
    }

    [HttpPost("{listId}/products")]
    public ActionResult<ProductData> PostProduct(Guid listId, [FromBody] ProductData body, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return NotFound();

        var listMember = user.ListMembers.SingleOrDefault(lm => lm.ListId == listId);
        if (listMember == null) return NotFound();

        var product = new Product {
            Id = Guid.NewGuid(),
            ListId = listId,
            Name = body.Name,
            Count = body.Count.Value,
            IsPurchased = false
        };
        db.Products.Add(product);

        db.SaveChanges();

        return new ProductData(product);
    }

        [HttpPatch("{listId}/products/{id}")]
    public ActionResult<ProductData> PatchProduct(Guid listId, Guid id, [FromBody] ProductData body, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return NotFound();

        var listMember = user.ListMembers.SingleOrDefault(lm => lm.ListId == listId);
        if (listMember == null) return NotFound();

        var product = listMember.ProductList.Products.SingleOrDefault(p => p.Id == id);

        if (body.Name != null) product.Name = body.Name;
        if (body.Count != null) product.Count = body.Count.Value;
        if (body.IsPurchased != null) product.IsPurchased = body.IsPurchased.Value;

        db.SaveChanges();

        return new ProductData(product);
    }

    [HttpDelete("{listId}/products/{id}")]
    public ActionResult<ProductIdData> DeleteProduct(Guid listId, Guid id, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return NotFound();

        var listMember = user.ListMembers.SingleOrDefault(lm => lm.ListId == listId);
        if (listMember == null) return NotFound();

        var product = listMember.ProductList.Products.SingleOrDefault(p => p.Id == id);

        db.Products.Remove(product);

        db.SaveChanges();

        return new ProductIdData(product);
    }
  }
}
