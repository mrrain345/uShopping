using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace uShopping.Models
{
    public partial class DBContext : DbContext
    {
        public DBContext()
        {
        }

        public DBContext(DbContextOptions<DBContext> options)
            : base(options)
        {
        }

        public virtual DbSet<ListMember> ListMembers { get; set; }
        public virtual DbSet<ProductList> ProductLists { get; set; }
        public virtual DbSet<Product> Products { get; set; }
        public virtual DbSet<Session> Sessions { get; set; }
        public virtual DbSet<User> Users { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<ListMember>(entity =>
            {
                entity.ToTable("ListMembers");
                entity.HasNoKey();

                entity.HasIndex(e => e.ListId)
                    .HasName("ListMembers_list_id_IDX")
                    .IsUnique();

                entity.HasIndex(e => e.UserId)
                    .HasName("ListMembers_user_id_IDX")
                    .IsUnique();

                entity.Property(e => e.ListId)
                    .IsRequired()
                    .HasColumnName("list_id")
                    .HasColumnType("varchar(36)")
                    .HasCharSet("ascii")
                    .HasCollation("ascii_general_ci");

                entity.Property(e => e.UserId)
                    .IsRequired()
                    .HasColumnName("user_id")
                    .HasColumnType("varchar(36)")
                    .HasCharSet("ascii")
                    .HasCollation("ascii_general_ci");

                entity.HasOne(d => d.ProductList)
                    .WithOne()
                    .HasForeignKey<ListMember>(d => d.ListId)
                    .HasConstraintName("ListMembers_FK_1");

                entity.HasOne(d => d.User)
                    .WithOne()
                    .HasForeignKey<ListMember>(d => d.UserId)
                    .HasConstraintName("ListMembers_FK");
            });

            modelBuilder.Entity<ProductList>(entity =>
            {
                entity.ToTable("ProductLists");
                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .HasColumnType("varchar(36)")
                    .HasCharSet("ascii")
                    .HasCollation("ascii_general_ci");

                entity.Property(e => e.CreatedAt)
                    .HasColumnName("created_at")
                    .HasColumnType("timestamp")
                    .HasDefaultValueSql("CURRENT_TIMESTAMP");

                entity.Property(e => e.Title)
                    .IsRequired()
                    .HasColumnName("title")
                    .HasColumnType("varchar(800)")
                    .HasCharSet("utf8mb4")
                    .HasCollation("utf8mb4_general_ci");
            });

            modelBuilder.Entity<Product>(entity =>
            {
                entity.ToTable("Products");
                entity.HasIndex(e => e.ListId)
                    .HasName("Products_list_id_IDX");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .HasColumnType("varchar(36)")
                    .HasCharSet("ascii")
                    .HasCollation("ascii_general_ci");

                entity.Property(e => e.Active).HasColumnName("active");

                entity.Property(e => e.Count)
                    .HasColumnName("count")
                    .HasColumnType("int(11)")
                    .HasDefaultValueSql("'1'");

                entity.Property(e => e.ListId)
                    .IsRequired()
                    .HasColumnName("list_id")
                    .HasColumnType("varchar(36)")
                    .HasCharSet("ascii")
                    .HasCollation("ascii_general_ci");

                entity.Property(e => e.Name)
                    .IsRequired()
                    .HasColumnName("name")
                    .HasColumnType("varchar(80)")
                    .HasCharSet("utf8mb4")
                    .HasCollation("utf8mb4_general_ci");

                entity.HasOne(d => d.ProductList)
                    .WithMany(p => p.Products)
                    .HasForeignKey(d => d.ListId)
                    .HasConstraintName("Products_FK");
            });

            modelBuilder.Entity<Session>(entity =>
            {
                entity.ToTable("Sessions");
                entity.HasKey(e => e.SessId)
                    .HasName("PRIMARY");

                entity.HasIndex(e => e.UserId)
                    .HasName("Sessions_user_id_IDX");

                entity.Property(e => e.SessId)
                    .HasColumnName("sess_id")
                    .HasColumnType("varchar(36)")
                    .HasCharSet("ascii")
                    .HasCollation("ascii_general_ci");

                entity.Property(e => e.CreatedAt)
                    .HasColumnName("created_at")
                    .HasColumnType("timestamp")
                    .HasDefaultValueSql("CURRENT_TIMESTAMP");

                entity.Property(e => e.UserId)
                    .IsRequired()
                    .HasColumnName("user_id")
                    .HasColumnType("varchar(36)")
                    .HasCharSet("ascii")
                    .HasCollation("ascii_general_ci");
            });

            modelBuilder.Entity<User>(entity =>
            {
                entity.ToTable("Users");
                entity.HasIndex(e => e.Email)
                    .HasName("Users_email_IDX")
                    .IsUnique();

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .HasColumnType("varchar(36)")
                    .HasCharSet("ascii")
                    .HasCollation("ascii_general_ci");

                entity.Property(e => e.Email)
                    .IsRequired()
                    .HasColumnName("email")
                    .HasColumnType("varchar(120)")
                    .HasCharSet("utf8")
                    .HasCollation("utf8_general_ci");

                entity.Property(e => e.Password)
                    .IsRequired()
                    .HasColumnName("password")
                    .HasColumnType("varchar(255)")
                    .HasCharSet("utf8mb4")
                    .HasCollation("utf8mb4_general_ci");

                entity.Property(e => e.Username)
                    .IsRequired()
                    .HasColumnName("username")
                    .HasColumnType("varchar(80)")
                    .HasCharSet("utf8mb4")
                    .HasCollation("utf8mb4_general_ci");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
