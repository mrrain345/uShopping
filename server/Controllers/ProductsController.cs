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
        if (user == null) return ErrorData.SessionError();

        var productList = user.GetProductList(listId);
        if (productList == null) NotFound();

        var products = productList.Products.OrderBy(p => p.Name).Select(p => new ProductData(p));
        return Ok(products);
    }

    [HttpPost("{listId}/products")]
    public ActionResult<ProductData> PostProduct(Guid listId, [FromBody] ProductData body, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return ErrorData.SessionError();

        if (!user.HasProductList(listId)) return NotFound();

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
        if (user == null) return ErrorData.SessionError();

        var product = user.GetProductList(listId)?.GetProduct(id);
        if (product == null) NotFound();

        if (body.Name != null) product.Name = body.Name;
        if (body.Count != null) product.Count = body.Count.Value;
        if (body.IsPurchased != null) product.IsPurchased = body.IsPurchased.Value;

        db.SaveChanges();

        return new ProductData(product);
    }

    [HttpDelete("{listId}/products/{id}")]
    public ActionResult<IdData> DeleteProduct(Guid listId, Guid id, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return ErrorData.SessionError();

        var product = user.GetProductList(listId)?.GetProduct(id);
        if (product == null) return NotFound();

        db.Products.Remove(product);

        db.SaveChanges();

        return new IdData(product.Id);
    }
  }
}
