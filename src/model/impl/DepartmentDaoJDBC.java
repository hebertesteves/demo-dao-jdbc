package model.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement("INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, obj.getName());
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                rs = ps.getGeneratedKeys();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void update(Department obj) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");
            ps.setString(1, obj.getName());
            ps.setInt(2, obj.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
            ps.setInt(1, id);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new DbException("Unexpected error! Id does not exist");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement("SELECT * FROM department WHERE Id = ?");
            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                return instantiateDepartment(rs);
            }
            return null;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.prepareStatement("SELECT * FROM department ORDER BY Name");

            rs = ps.executeQuery();
            List<Department> list = new ArrayList<>();

            while (rs.next()) {
                Department dep = instantiateDepartment(rs);
                list.add(dep);
            }

            return list;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(ps);
            DB.closeResultSet(rs);
        }
    }

    public Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();

        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));

        return dep;
    }
}
