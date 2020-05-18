using System;
using uShopping.Models;

namespace uShopping.Data {
  public class ProductData {
    public Guid Id { get; set; }
    public Guid ListId { get; set; }
    public string Name { get; set; }
    public int? Count { get; set; }
   public bool? IsPurchased { get; set; }

    public ProductData () {}
    public ProductData (Product product) {
        ListId = product.ListId;
        Id = product.Id;
        Name = product.Name;
        Count = product.Count;
        IsPurchased = product.IsPurchased;
    }
  }
}