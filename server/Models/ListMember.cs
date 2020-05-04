using System;
using System.Collections.Generic;

namespace uShopping.Models
{
    public partial class ListMember
    {
        public Guid UserId { get; set; }
        public Guid ListId { get; set; }
        public Guid Id { get; set; }

        public virtual ProductList ProductList { get; set; }
        public virtual User User { get; set; }
    }
}
