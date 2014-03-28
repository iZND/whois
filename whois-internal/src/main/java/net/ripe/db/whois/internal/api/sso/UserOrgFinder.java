package net.ripe.db.whois.internal.api.sso;

import com.google.common.collect.Sets;
import net.ripe.db.whois.common.dao.RpslObjectInfo;
import net.ripe.db.whois.common.dao.jdbc.JdbcRpslObjectOperations;
import net.ripe.db.whois.common.dao.jdbc.index.IndexStrategies;
import net.ripe.db.whois.common.domain.CIString;
import net.ripe.db.whois.common.domain.Maintainers;
import net.ripe.db.whois.common.rpsl.AttributeType;
import net.ripe.db.whois.common.rpsl.ObjectType;
import net.ripe.db.whois.common.rpsl.RpslObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Set;

@Component
public class UserOrgFinder {
    private final Maintainers maintainers;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserOrgFinder(@Qualifier("whoisReadOnlySlaveDataSource") final DataSource dataSource,
                         final Maintainers maintainers) {

        this.maintainers = maintainers;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // auth <- mntner <- org (mnt-by/mnt-ref)
    @Transactional
    public Set<RpslObject> findOrganisationsForAuth(final String auth) {
        final Set<RpslObject> result = Sets.newHashSet();

        List<RpslObjectInfo> mntnerIds = IndexStrategies.get(AttributeType.AUTH).findInIndex(jdbcTemplate, auth);

        for (final RpslObjectInfo mntnerId : mntnerIds) {
            final RpslObject mntner = JdbcRpslObjectOperations.getObjectById(jdbcTemplate, mntnerId.getObjectId());

            final Set<CIString> intersection = Sets.intersection(mntner.getValuesForAttribute(AttributeType.MNT_BY), maintainers.getPowerMaintainers());

            AttributeType inverseLookupAttr = intersection.isEmpty() ? AttributeType.MNT_BY : AttributeType.MNT_REF;

            final List<RpslObjectInfo> orgIds = IndexStrategies.get(inverseLookupAttr).findInIndex(jdbcTemplate, mntnerId, ObjectType.ORGANISATION);

            for (RpslObjectInfo orgId : orgIds) {
                result.add(JdbcRpslObjectOperations.getObjectById(jdbcTemplate, orgId.getObjectId()));
            }
        }

        return result;
    }
}