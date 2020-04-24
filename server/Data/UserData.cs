using System;
using uShopping.Models;

namespace uShopping.Data {
  public class UserData {
    
    public Guid Id { get; set; }
    public string Username { get; set; }
    public string Email { get; set; }

    public UserData() {}

    public UserData(User user) {
      Id = user.Id;
      Username = user.Username;
      Email = user.Email;
    }
  }
}