using System;
using uShopping.Models;

namespace uShopping.Data {
  public class ProductIdData {
    
    public Guid Id { get; set; }
    public ProductIdData () {}
    public ProductIdData (Product product) {
        Id = product.Id;
    }
  }
}