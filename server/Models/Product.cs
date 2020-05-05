using System;
using System.Collections.Generic;

namespace uShopping.Models
{
    public partial class Product
    {
        public Guid Id { get; set; }
        public Guid ListId { get; set; }
        public string Name { get; set; }
        public int Count { get; set; }
        public bool IsPurchased { get; set; }

        public virtual ProductList ProductList { get; set; }
    }
}
