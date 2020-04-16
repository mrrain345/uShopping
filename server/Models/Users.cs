using System;
using System.Collections.Generic;

namespace uShopping.Models
{
    public partial class Users
    {
        public Users()
        {
            Sessions = new HashSet<Sessions>();
        }

        public int Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        public string Email { get; set; }

        public virtual ICollection<Sessions> Sessions { get; set; }
    }
}
