using System;
using System.Collections.Generic;

namespace uShopping.Models
{
    public partial class User
    {
        public User()
        {
            Sessions = new HashSet<Session>();
        }

        public Guid Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        public string Email { get; set; }

        public virtual ListMember ListMember { get; set; }
        public virtual ICollection<Session> Sessions { get; set; }
    }
}
