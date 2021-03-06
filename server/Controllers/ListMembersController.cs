using Microsoft.AspNetCore.Mvc;
using uShopping.Models;
using uShopping.Data;
using System;
using System.Linq;
using System.Collections.Generic;

namespace uShopping.Controllers {

  public partial class ListsController : Controller {
    

    [HttpGet("{listId}/users")]
    public ActionResult<IEnumerable <UserData>> GetUsers(Guid listId, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return ErrorData.SessionError();

        var listMember = user.ListMembers.SingleOrDefault(lm => lm.ListId == listId);
        if (listMember == null) return NotFound();

        var users = user.GetProductList(listId).ListMembers.Select(lm => new UserData(lm.User));

        return Ok(users);
    }

    [HttpPost("{listId}/users")]
    public ActionResult<UserData> PostListMember(Guid listId, [FromBody] UserData body, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return ErrorData.SessionError();

        var listMember = user.ListMembers.SingleOrDefault(lm => lm.ListId == listId);
        if (listMember == null) return NotFound();

        var userToAdd = db.Users.SingleOrDefault(u => u.Email == body.Email);
        if (userToAdd == null) return NotFound();
        if (user.GetProductList(listId).ListMembers.SingleOrDefault(lm => lm.UserId == userToAdd.Id) != null)
        return BadRequest("User already added to this list");

        var newListMember = new ListMember {
            Id = Guid.NewGuid(),
            UserId = userToAdd.Id,
            ListId = listId
        };
        
        db.ListMembers.Add(newListMember);

        db.SaveChanges();

        return new UserData(userToAdd);
    }

    [HttpDelete("{listId}/users/{userId}")]
    public ActionResult<UserData> DeleteListMember(Guid listId, Guid userId, [FromHeader] Guid authorization) {
        User user = Session.GetUser(db, authorization);
        if (user == null) return ErrorData.SessionError();

        var listMember = user.ListMembers.SingleOrDefault(lm => lm.ListId == listId);
        if (listMember == null) return NotFound();

        var member = db.Users.SingleOrDefault(u => u.Id == userId);
        if (member == null) return NotFound();

        var listMemberToDelete = member.ListMembers.SingleOrDefault(lm => lm.ListId == listId);

        var productList = listMemberToDelete.ProductList;
        db.ListMembers.Remove(listMemberToDelete);

        if (productList.ListMembers.Count == 0) {
            db.Products.RemoveRange(productList.Products);
            db.ProductLists.Remove(productList);
        }

        db.SaveChanges();

        return new UserData(member);
    }
  }
}
