using System;
using uShopping.Models;

namespace uShopping.Data {
  public class ProductListIdData {
    
    public Guid Id { get; set; }
    public ProductListIdData () {}
    public ProductListIdData (ProductList productList) {
        Id = productList.Id;
    }
  }
}