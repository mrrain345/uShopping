using System;
using uShopping.Models;

namespace uShopping.Data {
  public class ProductListData {
    public Guid Id { get; set; }
    public string Title { get; set; }
    public DateTime CreatedAt { get; set; }

    public ProductListData () {}
    public ProductListData (ProductList productList) {
        Id = productList.Id;
        Title = productList.Title;
        CreatedAt = productList.CreatedAt;
    }
  }
}