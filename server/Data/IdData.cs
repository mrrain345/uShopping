using System;
using uShopping.Models;

namespace uShopping.Data {
  public class IdData {
    
    public Guid Id { get; set; }

    public IdData () {}
    public IdData (Guid id) {
        Id = id;
    }
  }
}