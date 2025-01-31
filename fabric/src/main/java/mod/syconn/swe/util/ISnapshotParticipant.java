package mod.syconn.swe.util;

import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public interface ISnapshotParticipant<T> extends Transaction.CloseCallback, Transaction.OuterCloseCallback {

    List<T> snapshots();
    T createSnapshot();
    void readSnapshot(T snapshot);
    void releaseSnapshot(T snapshot);
    void onFinalCommit();


    default void updateSnapshots(TransactionContext transaction) {
        while (snapshots().size() <= transaction.nestingDepth()) snapshots().add(null);

        if (snapshots().get(transaction.nestingDepth()) == null) {
            T snapshot = createSnapshot();
            Objects.requireNonNull(snapshot, "Snapshot may not be null!");
            snapshots().set(transaction.nestingDepth(), snapshot);
            transaction.addCloseCallback(this);
        }
    }

    default void onClose(TransactionContext transaction, Transaction.Result result) {
        T snapshot = snapshots().set(transaction.nestingDepth(), null);

        if (result.wasAborted()) {
            readSnapshot(snapshot);
            releaseSnapshot(snapshot);
        } else if (transaction.nestingDepth() > 0) {
            if (snapshots().get(transaction.nestingDepth() - 1) == null) {
                snapshots().set(transaction.nestingDepth() - 1, snapshot);
                transaction.getOpenTransaction(transaction.nestingDepth() - 1).addCloseCallback(this);
            } else releaseSnapshot(snapshot);
        } else {
            releaseSnapshot(snapshot);
            transaction.addOuterCloseCallback(this);
        }
    }

    default void afterOuterClose(Transaction.Result result) {
        onFinalCommit();
    }
}
