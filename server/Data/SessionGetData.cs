using System;
using uShopping.Models;

namespace uShopping.Data {
  public class SessionGetData {

    public bool Active { get; set; }
    public UserData User { get; set; }

    public SessionGetData() {}

    public SessionGetData(User user) {
        if (user != null) {
            Active = true;
            User = new UserData(user);
        } else {
            Active = false;
        }
    }
  }
}