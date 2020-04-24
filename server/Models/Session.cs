using System;
using System.Collections.Generic;

namespace uShopping.Models
{
    public partial class Session
    {
        public Guid SessId { get; set; }
        public Guid UserId { get; set; }
        public DateTime CreatedAt { get; set; }
    }
}
