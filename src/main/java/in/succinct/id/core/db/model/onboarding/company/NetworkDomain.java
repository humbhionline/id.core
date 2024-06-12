package in.succinct.id.core.db.model.onboarding.company;

import com.venky.core.util.ObjectUtil;
import com.venky.swf.db.annotations.column.UNIQUE_KEY;
import com.venky.swf.db.annotations.model.CONFIGURATION;
import com.venky.swf.db.annotations.model.HAS_DESCRIPTION_FIELD;
import com.venky.swf.db.annotations.model.MENU;
import com.venky.swf.db.model.Model;
import com.venky.swf.sql.Conjunction;
import com.venky.swf.sql.Expression;
import com.venky.swf.sql.Operator;
import com.venky.swf.sql.Select;

import java.util.List;
import java.util.Optional;

@CONFIGURATION
@HAS_DESCRIPTION_FIELD("DESCRIPTION")
@MENU("Beckn")
public interface NetworkDomain extends Model {
    @UNIQUE_KEY
    public String getName();
    public void setName(String name);

    @UNIQUE_KEY("CODE")
    public String getCode();
    public void setCode(String code);

    public String getDescription();
    public void setDescription(String description);

    public String getSchemaUrl();
    public void setSchemaUrl(String schemaUrl);

    public static NetworkDomain find(String nameOrCode){
        Select select = new Select().from(NetworkDomain.class);
        select.where(new Expression(select.getPool(), Conjunction.OR).
                add(new Expression(select.getPool(),"CODE", Operator.EQ,nameOrCode)).
                add(new Expression(select.getPool(),"NAME",Operator.EQ,nameOrCode)));

        List<NetworkDomain> domainList = select.execute();
        if (domainList.isEmpty()) {
            return null;
        }
        if (domainList.size() == 1){
            return domainList.get(0);
        }
        Optional<NetworkDomain> optionalNetworkDomain = domainList.stream().filter(d->ObjectUtil.equals(d.getCode(),nameOrCode)).findAny();
        return optionalNetworkDomain.orElse(null);
    }
}