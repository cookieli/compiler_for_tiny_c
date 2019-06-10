.file "tests/codeGenOfMe/if_02.dcf"
.text
.section .rodata
.LC0:
.string "a, b is %d, %d\n"
.LC1:
.string "hello world\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $32, %rsp
movq $2,-17(%rbp)
movq $3,-9(%rbp)
movb $1,-1(%rbp)
movb -1(%rbp),%al
cmpb $0,%al
jle .L1
movq -9(%rbp),%rdx
movq -17(%rbp),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
jmp .L2
.L1:
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
.L2:
leave
ret

