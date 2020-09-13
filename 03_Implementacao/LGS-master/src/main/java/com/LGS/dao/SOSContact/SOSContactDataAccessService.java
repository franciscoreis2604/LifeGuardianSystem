package com.LGS.dao.SOSContact;

import com.LGS.exception.ApiRequestException;
import com.LGS.model.SOSContact.SOSContactBundle;
import com.LGS.model.SOSContact.SOSContactPacket;
import com.LGS.model.View.SOSContact.SOSContactBundleView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres_sosContacts")
public class SOSContactDataAccessService implements SOSContactDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SOSContactDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean insertSOSContact(SOSContactBundle sosContactBundle) {
        if (sosContactBundle.getContactInfo().size() == 0)
            throw new ApiRequestException("Contact info cannot be empty");
        try {
            StringBuilder query = new StringBuilder("INSERT INTO SOSContacts (uId,cName,cType,cValue) VALUES ");
            for (SOSContactPacket packet : sosContactBundle.getContactInfo()) {
                query.append(String.format("('%s','%s','%s','%s'),", sosContactBundle.getUId(), sosContactBundle.getName(), packet.getContactType(), packet.getContactValue()));
            }
            query = new StringBuilder(query.substring(0, query.length() - 1)); // Remove the last ',' from the query
            jdbcTemplate.update(query.toString());
            return true;
        } catch (Exception e) {
            throw new ApiRequestException("Couldn't insert SOS Contact");
        }
    }


    @Override
    public Optional<List<SOSContactBundleView>> selectSOSContactByUserId(UUID uId) {
        try {
            String query = "SELECT * FROM SOSContacts WHERE uId = ? ORDER BY cName ASC";
            List<SOSContactBundleView> sosContacts = jdbcTemplate.query(query, new Object[]{uId},
                    (resultSet, i) -> {
                        String name = resultSet.getString("cName");
                        String type = resultSet.getString("cType");
                        String value = resultSet.getString("cValue");
                        return new SOSContactBundleView(name, new SOSContactPacket(type, value));
                    });
            return Optional.of(sosContacts);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private int updateSOSContactPacket(UUID uId, String name, SOSContactPacket sosContactPacket) {
        String sql = "UPDATE SOSContacts SET cValue = ? WHERE uid = ? AND cName = ? AND cType = ?";
        return jdbcTemplate.update(sql, sosContactPacket.getContactValue(), uId, name, sosContactPacket.getContactType());
    }

    @Override
    public int updateSOSContactBundle(SOSContactBundle sosContactBundle) {
        if (sosContactBundle.getContactInfo().size() == 0)
            throw new ApiRequestException("Contact info cannot be empty");
        try {
            for (SOSContactPacket packet : sosContactBundle.getContactInfo()) {
                int value = updateSOSContactPacket(sosContactBundle.getUId(), sosContactBundle.getName(), packet);
                if (value == 0) {
                    throw new ApiRequestException("Couldn't update contact list");
                }
            }
            return 1;
        } catch (Exception e) {
            throw new ApiRequestException("Couldn't update contacts");
        }
    }

    private int deleteSOSContactPacket(UUID uId, String name, SOSContactPacket contactPacket) {
        String sql = "DELETE FROM SOSContacts WHERE uid = ? AND cName = ? AND cType = ?";
        return jdbcTemplate.update(sql, uId, name, contactPacket.getContactType());
    }

    @Override
    public int deleteSOSContactBundle(SOSContactBundle sosContactBundle) {
        if (sosContactBundle.getContactInfo().size() == 0)
            throw new ApiRequestException("Contact info cannot be empty");
        try {
            for (SOSContactPacket packet : sosContactBundle.getContactInfo()) {
                int value = deleteSOSContactPacket(sosContactBundle.getUId(), sosContactBundle.getName(), packet);
                if (value == 0) {
                    throw new ApiRequestException("Couldn't delete contact list");
                }
            }
            return 1;
        } catch (Exception e) {
            throw new ApiRequestException("Couldn't delete contact list");
        }
    }
}
