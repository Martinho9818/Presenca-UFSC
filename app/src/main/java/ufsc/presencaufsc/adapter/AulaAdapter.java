package ufsc.presencaufsc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ufsc.presencaufsc.R;
import ufsc.presencaufsc.model.Aula;

public class AulaAdapter extends RecyclerView.Adapter<AulaAdapter.ViewHolderAula> {

    private List<Aula> dadosAula;

    public AulaAdapter(List<Aula> pdadosAula) {
        this.dadosAula = pdadosAula;
    }

    @NonNull
    @Override
    public AulaAdapter.ViewHolderAula onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.linha_aula, parent, false);
        ViewHolderAula holderAula = new ViewHolderAula(view, parent.getContext());
        return holderAula;
    }

    @Override
    public void onBindViewHolder(@NonNull AulaAdapter.ViewHolderAula holder, int position) {
        if ((dadosAula != null) && (dadosAula.size() > 0)) {
            Aula aula = dadosAula.get(position);
            holder.txtDataAula.setText(new StringBuilder().append("Data: ").append(aula.getDataAula()).toString());
            holder.txtHoraAula.setText(new StringBuilder().append("Hora: ").append(aula.getHoraAula()).toString());
            holder.txtCodigoDisciplinaAula.setText(new StringBuilder().append("Código Disciplina: ").append(aula.getCod_disciplina()).toString());
        }
    }

    @Override
    public int getItemCount() {
        return dadosAula.size();
    }

    public class ViewHolderAula extends RecyclerView.ViewHolder {

        public TextView txtDataAula;
        public TextView txtHoraAula;
        public TextView txtCodigoDisciplinaAula;

        public ViewHolderAula(View itemView, final Context context) {
            super(itemView);
            txtDataAula = (TextView) itemView.findViewById(R.id.textViewDataAula);
            txtHoraAula = (TextView) itemView.findViewById(R.id.textViewHorarioAula);
            txtCodigoDisciplinaAula = (TextView) itemView.findViewById(R.id.textViewCodDisciplinaAula);
        }
    }
}
