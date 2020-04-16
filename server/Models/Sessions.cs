using System;
using System.Collections.Generic;

namespace uShopping.Models
{
    public partial class Sessions
    {
        public string SessId { get; set; }
        public int UserId { get; set; }
        public DateTime CreatedAt { get; set; }

        public virtual Users User { get; set; }
    }
}
