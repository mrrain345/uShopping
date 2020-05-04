using System;
using System.Collections.Generic;

namespace uShopping.Models
{
    public partial class ProductList
    {
        public ProductList()
        {
            ListMembers = new HashSet<ListMember>();
            Products = new HashSet<Product>();
        }

        public Guid Id { get; set; }
        public string Title { get; set; }
        public DateTime CreatedAt { get; set; }

        public virtual ICollection<ListMember> ListMembers { get; set; }
        public virtual ICollection<Product> Products { get; set; }
    }
}
