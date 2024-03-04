package me.x.check.checks.reach;

import me.x.check.ReachCheck;
import me.x.check.annotation.CheckInfo;
import me.x.check.type.CheckType;
import me.x.check.version.CheckVersion;
import me.x.compat.NMSManager;
import me.x.compat.ServerVersion;
import me.x.data.reach.DistanceData;
import me.x.data.reach.ReachBase;

@CheckInfo(
        type = CheckType.REACH,
        subType = "A",
        friendlyName = "Reach",
        version = CheckVersion.RELEASE,
        maxViolations = 30,
        minViolations = -1.5,
        logData = true
)
public class ReachA extends ReachCheck {
    private long lastFlag;

    public ReachA() {
        if (NMSManager.getInstance().getServerVersion().after(ServerVersion.v1_8_R3)) {
            this.setMaxViolation(100);
        }
    }

    public void handle(ReachBase reachBase, long l) {
        DistanceData distanceData = reachBase.getDistanceData();
        double reach = distanceData.getReach();
        double extra = distanceData.getExtra();
        double vertical = distanceData.getVertical();
        double horizontal = distanceData.getHorizontal();
        if (reach > 3.0 && horizontal < 6.0 && extra < 6.0) {
            long timestamp = l - this.lastFlag;
            if (timestamp < 50L) {
                return;
            }
            this.handleViolation(String.format("R: %.2f H: %.2f V: %.2f E: %.2f S: %s C: %.2f P: %s",
                    reach, horizontal, vertical, extra, reachBase.getDistanceList().size(), reachBase.getCertainty(), this.playerData.getTransactionPing()),
                    Math.min(reach, 4.5) - 3.0 + reachBase.getCertainty());
        } else {
            this.violations -= Math.min(this.violations + 1.5, 0.005);
        }
    }

}