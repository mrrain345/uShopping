using System;
using uShopping.Models;
using System.Text.Json.Serialization;

namespace uShopping.Data {
  public class ProductData {
    public Guid Id { get; set; }
    public Guid ListId { get; set; }
    public string Name { get; set; }
    public int? Count { get; set; }
    [JsonPropertyName("is_purcheased")] public bool? IsPurchased { get; set; }

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