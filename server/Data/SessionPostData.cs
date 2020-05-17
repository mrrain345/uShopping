using System;
using uShopping.Models;

namespace uShopping.Data {
  public class SessionPostData {

    public Guid Session { get; set; }
    public UserData User { get; set; }

    public SessionPostData() {}

    public SessionPostData(Guid session, User user) {
      Session = session;
      User = new UserData(user);
    }
  }
}