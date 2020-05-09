using System;
using System.Collections.Generic;
using System.Linq;

namespace uShopping.Models
{
    public partial class User
    {
        public User()
        {
            ListMembers = new HashSet<ListMember>();
            Sessions = new HashSet<Session>();
        }

        public Guid Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        public string Email { get; set; }

        public virtual ICollection<ListMember> ListMembers { get; set; }
        public virtual ICollection<Session> Sessions { get; set; }

        public IEnumerable<ProductList> GetProductLists() {
            return ListMembers.Select(lm => lm.ProductList);
        }

        public ProductList GetProductList(Guid listId) {
            var listMember = ListMembers.SingleOrDefault(lm => lm.ListId == listId);
            return listMember?.ProductList;
        }

        public bool HasProductList(Guid listId) {
            return ListMembers.SingleOrDefault(lm => lm.ListId == listId) != null;
        }
    }
}
