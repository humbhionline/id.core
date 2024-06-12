package in.succinct.id.core.extensions;

import com.venky.core.util.ObjectUtil;
import com.venky.swf.db.extensions.ModelOperationExtension;
import com.venky.swf.plugins.collab.db.model.user.UserPhone;
import in.succinct.id.core.db.model.user.User;

public class UserPhoneExtension extends ModelOperationExtension<UserPhone> {
    static {
        registerExtension(new UserPhoneExtension());
    }
    @Override
    public void afterCreate(UserPhone model) {
        if (!model.isValidated()){
            model.sendOtp();
        }else{
            User user = model.getUser().getRawRecord().getAsProxy(User.class);
            if (ObjectUtil.equals(user.getPhoneNumber(),model.getPhoneNumber())){
                user.setPhoneNumberVerified(true);
                user.setTxnProperty("being.verified",true);
                user.save();
            }
        }
    }
}
